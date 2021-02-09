package lark.msg.api;

import lark.msg.MsgHandlerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandlerApi {
    MsgHandlerService handlerService;
    public HandlerApi(MsgHandlerService handlerService ) {
        this.handlerService = handlerService;
    }

    @PostMapping("/run/{name}")
    public MsgHandlerService.ExecuteResult execute(@PathVariable("name") String name, @RequestBody(required = false) String data ) {
        return handlerService.execute( name, data );
    }
}
