package com.karasik.modules;

import com.google.inject.AbstractModule;
import com.karasik.dao.MongoDao;
import com.karasik.dao.RedisDao;
import com.karasik.service.LoginService;
import com.karasik.service.RegisterService;

public class AuthenticationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RegisterService.class);
        bind(LoginService.class);
        bind(MongoDao.class).asEagerSingleton();
        bind(RedisDao.class).asEagerSingleton();
    }
}
