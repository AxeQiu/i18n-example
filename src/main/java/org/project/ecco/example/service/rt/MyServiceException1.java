package org.project.ecco.example.service.rt;

public class MyServiceException1
    extends RuntimeException
    implements I18nMessage
{
    private final String KEY = "errors.service.1";

    @Override
    public String getMessageKey() {
        return KEY;
    }
}
