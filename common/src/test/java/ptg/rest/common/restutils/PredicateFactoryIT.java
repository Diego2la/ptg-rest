package ptg.rest.common.restutils;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import ptg.rest.common.restutils.model.QSampleEntity;
import ptg.rest.common.restutils.model.SampleEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static ptg.rest.common.restutils.PredicateUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PredicateFactoryIT {

    private static final PredicateFactory predicateFactory = new PredicateFactoryImpl.Builder()
            .bind("stringField", QSampleEntity.sampleEntity.stringField::containsIgnoreCase)
            .bind("dateField", (s) -> createPredicateForComparableType(QSampleEntity.sampleEntity.dateField, FilterDataForComparableType.fromString(s, FilterDataForComparableType::localDateConverter)))
            .bind("enumField", (s) -> createPredicateForSimpleType(QSampleEntity.sampleEntity.enumField, FilterDataForSimpleType.fromString(s, SampleEntity.SampleEnum::valueOf)))
            .bind("intField", (s) -> createPredicateForNumericType(QSampleEntity.sampleEntity.intField, FilterDataForComparableType.fromString(s, Integer::parseInt)))
            .build();
    private static SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;

    @BeforeClass
    public static void setUp() {
        Configuration configuration = new Configuration();
        // Hibernate settings equivalent to hibernate.cfg.xml's properties
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "org.h2.Driver");
        settings.put(Environment.URL, "jdbc:h2:mem:test");
        settings.put(Environment.USER, "sa");
        settings.put(Environment.PASS, "");
        settings.put(Environment.POOL_SIZE, 1);
        settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "create-drop");
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(SampleEntity.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Before
    public void startTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @After
    public void rollback() {
        transaction.rollback();
    }

    @Test
    public void testMappingFor() {
        SampleEntity entity1 = new SampleEntity();
        entity1.setIntField(1);
        SampleEntity entity2 = new SampleEntity();
        entity2.setIntField(2);
        SampleEntity entity3 = new SampleEntity();
        entity3.setIntField(null);
        session.save(entity1);
        session.save(entity2);
        session.save(entity3);

        HibernateQueryFactory queryFactory = new HibernateQueryFactory(session);

        List<SampleEntity> entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("intField", "0~1")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity1.getId());

        entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("intField", "NULL")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity3.getId());

        entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("intField", "1")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity1.getId());

        entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("intField", "~3")))
                .fetch();

        assertThat(entities.size()).isEqualTo(2);
    }

    @Test
    public void testMappingForString() {
        SampleEntity entity1 = new SampleEntity();
        entity1.setStringField("foofoo");
        SampleEntity entity2 = new SampleEntity();
        entity2.setStringField("buzzbuzz");
        session.save(entity1);
        session.save(entity2);

        HibernateQueryFactory queryFactory = new HibernateQueryFactory(session);

        List<SampleEntity> entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("stringField", "ofo")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity1.getId());
    }

    @Test
    public void testMappingForDate() {
        SampleEntity entity1 = new SampleEntity();
        entity1.setDateField(LocalDate.of(2020, 2, 20));
        SampleEntity entity2 = new SampleEntity();
        entity2.setDateField(LocalDate.of(2020, 2, 25));
        session.save(entity1);
        session.save(entity2);

        HibernateQueryFactory queryFactory = new HibernateQueryFactory(session);

        List<SampleEntity> entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("dateField", "2020-02-19")))
                .fetch();

        assertThat(entities.size()).isEqualTo(0);

        entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("dateField", "2020-02-23~")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity2.getId());

        entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("dateField", "2020-02-23~2020-02-26")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity2.getId());
    }

    @Test
    public void testMappingForEnum() {
        SampleEntity entity1 = new SampleEntity();
        entity1.setEnumField(SampleEntity.SampleEnum.FIRST);
        SampleEntity entity2 = new SampleEntity();
        entity2.setEnumField(SampleEntity.SampleEnum.SECOND);
        session.save(entity1);
        session.save(entity2);

        HibernateQueryFactory queryFactory = new HibernateQueryFactory(session);

        List<SampleEntity> entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("enumField", "FIRST")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity1.getId());

        entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("enumField", "!FIRST")))
                .fetch();

        assertThat(entities.size()).isEqualTo(1);
        assertThat(entities.get(0).getId()).isEqualTo(entity2.getId());

        entities = queryFactory
                .selectFrom(QSampleEntity.sampleEntity)
                .where(predicateFactory.makePredicate(Collections.singletonMap("enumField", "!FIRST,SECOND")))
                .fetch();

        assertThat(entities.size()).isEqualTo(0);
    }
}
