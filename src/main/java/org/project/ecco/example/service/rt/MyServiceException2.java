package org.project.ecco.example.service.rt;

public class MyServiceException2
    extends RuntimeException
    implements I18nMessage
{
    private final String KEY = "errors.service.2";

    @Override
    public String getMessageKey() {
        return KEY;
    }
}
