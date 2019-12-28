package org.project.ecco.example.service.rt;

public class MyServiceException1
    extends RuntimeException
    implements I18nMessage
{
    @Override
    public String getMessageKey() {
        return "errors.service.1";
    }
}
