package com.springapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: vikramgoyal
 * Date: 7/22/13
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LoginController {

    @RequestMapping(value="/", method = RequestMethod.GET)
        public String printWelcome(ModelMap model) {
            //model.addAttribute("message", "Hello world!");
            return "login";

    }

    @RequestMapping(value="/login" , method = RequestMethod.POST)
       public String loginUser(ModelMap model) {
        //model.addAttribute("message", "Hello world!");
        //return new ModelAndView(new RedirectView("template"));
          /*ModelAndView modelAndView = new ModelAndView();
            RedirectView redirectView = new RedirectView("template");
            modelAndView.setView(redirectView);
          //return modelAndView;*/
           return "template";
    }
    /*@RequestMapping(value="/loh")
    public String ll(ModelMap model){
        return  "template";
    }
*/
}

