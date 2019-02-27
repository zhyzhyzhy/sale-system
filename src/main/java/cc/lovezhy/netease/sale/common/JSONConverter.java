package cc.lovezhy.netease.sale.common;

import cc.lovezhy.netease.sale.exception.HttpResponse;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
public class JSONConverter extends MappingJackson2HttpMessageConverter {

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (!(object instanceof HttpResponse)) {
            object = HttpResponse.success(object);
        }
        super.writeInternal(object, type, outputMessage);
    }
}
