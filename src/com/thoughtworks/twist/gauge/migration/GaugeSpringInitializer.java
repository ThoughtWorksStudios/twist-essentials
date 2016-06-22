package com.thoughtworks.twist.gauge.migration;

import com.thoughtworks.gauge.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;

public class GaugeSpringInitializer {

    private static AnnotationConfigApplicationContext elementContext;
    private static final String APPLICATION_CONTEXT_SUITE_XML = "applicationContext-suite.xml";
    private static final String APPLICATION_CONTEXT_SCENARIO_XML = "applicationContext-scenario.xml";
    private static final String APPLICATION_CONTEXT_SPEC_XML = "applicationContext-spec.xml";
    private static ClassPathXmlApplicationContext suiteContext;
    private static ClassPathXmlApplicationContext scenarioContext;
    private static ClassPathXmlApplicationContext specContext;

    public GaugeSpringInitializer() {
        elementContext = new AnnotationConfigApplicationContext();
    }

    // Sets the Class Initializer for Gauge-Java
    // Name of method starts with aa, just to make sure its executed before any other hooks.
    @BeforeSuite
    public void aabeforeSuite() {
        ClassInstanceManager.setClassInitializer(new ClassInitializer() {
            @Override
            public Object initialize(Class<?> aClass) throws Exception {
                registerJavaBean(getBeanName(aClass.getSimpleName()), aClass, elementContext, false);
                return bean(getBeanName(aClass.getSimpleName()));
            }
        });
        suiteContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_SUITE_XML);
        suiteContext.registerShutdownHook();
        suiteContext.start();
        elementContext.setParent(suiteContext);
        RootBeanDefinition beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
        elementContext.registerBeanDefinition(".scriptFactoryPostProcessor", beanDefinition);
        elementContext.refresh();
        elementContext.start();
    }

    @AfterSuite
    public void aaafterSuite() {
        if (suiteContext != null) {
            suiteContext.close();
            suiteContext = null;
        }
    }

    @BeforeSpec
    public void aabeforeSpec() {
        specContext = new ClassPathXmlApplicationContext(new String[]{APPLICATION_CONTEXT_SPEC_XML}, suiteContext);
        specContext.setParent(suiteContext);
        specContext.refresh();
        specContext.registerShutdownHook();
        specContext.start();
        elementContext = new AnnotationConfigApplicationContext();
        elementContext.setParent(specContext);
        RootBeanDefinition beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
        elementContext.registerBeanDefinition(".scriptFactoryPostProcessor", beanDefinition);
        elementContext.refresh();
        elementContext.start();
    }
    
    @BeforeScenario
    public void aabeforeScenario() {
        scenarioContext = new ClassPathXmlApplicationContext(new String[]{APPLICATION_CONTEXT_SCENARIO_XML}, specContext);
        scenarioContext.setParent(specContext);
        scenarioContext.refresh();
        scenarioContext.registerShutdownHook();
        scenarioContext.start();
        elementContext = new AnnotationConfigApplicationContext();
        elementContext.setParent(scenarioContext);
        RootBeanDefinition beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
        elementContext.registerBeanDefinition(".scriptFactoryPostProcessor", beanDefinition);
        elementContext.refresh();
        elementContext.start();
    }
    

    @AfterScenario
    public void aaafterScenario() {
        if (scenarioContext != null) {
            scenarioContext.close();
            scenarioContext = null;
        }
    }
    

    @AfterSpec
    public void aaafterSpec() {
        if (specContext != null) {
            specContext.close();
            specContext = null;
        }
    }

    private void registerJavaBean(String beanId, Class<?> aClass, GenericApplicationContext context, boolean lazy) {
        String beanName = getBeanName(beanId);
        boolean containsDefinition = context.containsBeanDefinition(beanName);
        if (containsDefinition) {
            return;
        }
        RootBeanDefinition beanDefinition = new RootBeanDefinition(aClass);
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_AUTODETECT);
        beanDefinition.setLazyInit(lazy);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        context.registerBeanDefinition(beanName, beanDefinition);
    }

    private static String getBeanName(String name) {
        if (name.length() <= 1) {
            return name.toLowerCase();
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    @SuppressWarnings("unchecked")
    public synchronized Object bean(String name) {
        String beanName = getBeanName(name);
               if (elementContext.containsBean(beanName)) {
                return elementContext.getBean(beanName);
        }
                
        return null;
    }
}
