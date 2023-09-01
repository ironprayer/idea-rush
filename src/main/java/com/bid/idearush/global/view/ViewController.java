package com.bid.idearush.global.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

    @GetMapping("/pageDetail")
    public String ideaDetailPage() {
        return "page/ideaDetail";
    }

    @GetMapping("/pageWrite")
    public String ideaWritePage() {
        return "page/ideaWrite";
    }

    @GetMapping("/pageUpdate")
    public String ideaUpdatePage() {
        return "page/ideaUpdate";
    }
}
