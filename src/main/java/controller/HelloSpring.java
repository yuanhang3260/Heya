package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloSpring {

  @RequestMapping("/heyatest")
  public String hello() {
    System.out.println("Hello Sring MVC");
    return "test";
  }
}
