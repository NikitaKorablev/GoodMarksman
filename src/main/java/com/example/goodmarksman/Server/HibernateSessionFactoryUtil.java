package com.example.goodmarksman.Server;

import com.example.goodmarksman.objects.Score;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().
                        configure("hibernate.cfg.xml");
                configuration.addAnnotatedClass(Score.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                        applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.err.println("HibernateSessionFactoryUtil.getSessionFactory() caught " + e.getMessage());
            }
        }

        return sessionFactory;
    }
}
