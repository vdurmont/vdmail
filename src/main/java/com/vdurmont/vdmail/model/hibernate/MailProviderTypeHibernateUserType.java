package com.vdurmont.vdmail.model.hibernate;


import com.vdurmont.vdmail.model.MailProviderType;

public class MailProviderTypeHibernateUserType extends EnumWithCodeHibernateUserType<MailProviderType> {
    @Override
    public Class returnedClass() {
        return MailProviderType.class;
    }

}
