package com.proper.enterprise.platform.api.auth;

import com.proper.enterprise.platform.api.auth.enums.GenderType;
import com.proper.enterprise.platform.core.api.IBase;

public interface Person extends IBase {

    GenderType getGender();

    void setGender(GenderType gender);

    String getIdCard();

    void setIdCard(String idCard);

    String getName();

    void setName(String name);

}
