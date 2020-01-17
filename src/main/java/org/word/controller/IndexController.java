package org.word.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.word.model.result.Result;
import org.word.service.TransformService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author gengkeke
 * @date 2020/1/17 10:02
 * @Description
 */
@Slf4j
@Controller
public class IndexController {
    @Autowired
    private TransformService transformService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private Integer port;

    @RequestMapping("/")
    public String getWord(Model model, @RequestParam(value = "url") String url,
                          @RequestParam(value = "download", required = false, defaultValue = "1") Integer download) throws IOException {
        Result transform = transformService.transform(url);
        model.addAttribute("url", url);
        model.addAttribute("download", download);
        model.addAttribute("result", transform);
        return "index";
    }

    /**
     * 将 swagger 文档一键下载为 doc 文档
     *
     * @param url      需要转换成 word 文档的资源地址
     * @param response
     */
    @RequestMapping("/downloadWord")
    public void word(@RequestParam(required = false) String url, HttpServletResponse response) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + port + "?download=0&url=" + url, String.class);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("toWord.doc", "utf-8"));
            byte[] bytes = forEntity.getBody().getBytes();
            bos.write(bytes, 0, bytes.length);
            bos.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


}
