package com.works.services;

import com.works.entities.User;
import com.works.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    final UserRepository uRepo;
    final HttpServletRequest req;
    final HttpServletResponse res;
    public UserService(UserRepository uRepo, HttpServletRequest req, HttpServletResponse res) {
        this.uRepo = uRepo;
        this.req = req;
        this.res = res;
    }


    public boolean loginControl(User user, String remember_me) {

        Optional<User> oUser = uRepo.findByEmailEqualsAndPasswordEqualsAllIgnoreCase(user.getEmail(), user.getPassword());
        if (oUser.isPresent() ) {
            User u = oUser.get();
            u.setPassword(null);
            // create session
            req.getSession().setAttribute("user", u );

            // Cookie control and create
            if ( !remember_me.equals("") ) {
                Cookie cookie = new Cookie("user",  sifrele(""+u.getId(), 5) );
                cookie.setMaxAge( 60 * 60 * 3 );
                res.addCookie(cookie);
            }

            return true;
        }else {
            return false;
        }
    }


    // user session control
    public String control( String page ) {
        cookie_control();
        boolean status = req.getSession().getAttribute("user") != null;
        if ( status ) {
            return page;
        }

        return "redirect:/login";
    }


    public void cookie_control() {
        // cookie control
        if ( req.getCookies() != null ) {
            Cookie[] cookies = req.getCookies();
            for ( Cookie item : cookies ) {
                if ( item.getName().equals("user") ) {
                    String val = item.getValue();
                    try {
                        val = sifreCoz(val, 5);
                        int uid = Integer.parseInt(val);
                        Optional<User> oUser = uRepo.findById(uid);
                        if (oUser.isPresent() ) {
                            User u = oUser.get();
                            u.setPassword(null);
                            req.getSession().setAttribute("user", u);
                        }
                    }catch (Exception ex) {

                    }
                    break;
                }
            }
        }
    }

    //logout
    public void logOut(){
        req.getSession().invalidate();
        Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }


    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String sifrele(String data, int i) {
        byte[] dizi = null;
        Random rd = new Random();
        int ri = rd.nextInt(899) + 100;
        for (int j = 0; j < i; j++) {
            dizi = Base64.getEncoder().encode(data.getBytes());
            data = new String(dizi);
        }
        String sifrelenmis = new String(dizi) + MD5("" + ri);
        System.out.println("sifrelenmis" + sifrelenmis);
        return sifrelenmis;
    }

    public static String sifreCoz(String data, int i) {
        byte[] dizi = null;
        data = data.substring(0, data.length() - 32);
        for (int j = 0; j < i; j++) {
            dizi = Base64.getDecoder().decode(data.getBytes());
            data = new String(dizi);
        }
        String cozulmus = new String(dizi);
        System.out.println("cozulmus" + cozulmus);
        return cozulmus;
    }



}