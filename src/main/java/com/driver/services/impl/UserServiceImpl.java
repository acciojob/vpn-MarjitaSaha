package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        String countryNameUpperCase = countryName.toUpperCase();
        if (!countryNameUpperCase.equals("IND") && !countryNameUpperCase.equals("USA") && !countryNameUpperCase.equals("AUS") && !countryNameUpperCase.equals("CHI") && !countryNameUpperCase.equals("JPN")) throw new Exception("Country not found");
        Country country = new Country();
        country.setCountryName(CountryName.valueOf(countryNameUpperCase));
        country.setCode(CountryName.valueOf(countryNameUpperCase).toCode());
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);
        user.setOriginalIp(country.getCode()+"."+user.getId());
        user.setOriginalCountry(country);
        country.setUser(user);
        userRepository3.save(user);
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
      User user=userRepository3.findById(userId).get();
      ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();
      user.getServiceProviderList().add(serviceProvider);
      serviceProvider.getUsers().add(user);
      userRepository3.save(user);
      serviceProviderRepository3.save(serviceProvider);
      return user;
    }
}
