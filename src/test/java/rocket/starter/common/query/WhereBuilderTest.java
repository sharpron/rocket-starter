package rocket.starter.common.query;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import rocket.starter.common.query.TestRealm.EmptyToken;
import rocket.starter.common.query.Where.Type;

/**
 * @author ron 2022/10/25
 */
@ExtendWith(MockitoExtension.class)
public class WhereBuilderTest {


  @Mock
  Root<Object> root;

  @Mock
  CriteriaBuilder criteriaBuilder;

  @Mock
  CriteriaQuery<?> criteriaQuery;

  @Mock
  Predicate predicate;

  @Data
  static class BasicExample {

    @Where(type = Type.like)
    private String nameLike;
    @Where(type = Type.left_like)
    private String nameLeftLike;
    @Where(type = Type.right_like)
    private String nameRightLik;
    @Where(type = Type.eq)
    private String nameEq;
    @Where(type = Type.lt)
    private Integer ageLt;
    @Where(type = Type.gt)
    private Integer ageGt;
    @Where(type = Type.le)
    private Integer ageLe;
    @Where(type = Type.ge)
    private Integer ageGe;
    @Where(type = Type.between)
    private List<Long> ageBetween;
    @Where(type = Type.betweenTime)
    private List<Long> birthdayBetweenTime;
    @Where(type = Type.in)
    private List<Long> nameIn;
  }

  @BeforeEach
  public void beforeAll() {
    Mockito.when(criteriaBuilder.like(root.get("nameLike"), "%nameLike%"))
        .thenReturn(predicate);
  }


  @Data
  static class EmptyExample {

    private String name;
  }

  @Test
  public void testBuildSpec() {
    Specification<Object> specification = WhereBuilder.buildSpec(new EmptyExample());
    Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
    Assertions.assertNull(predicate);

    BasicExample basicExample = new BasicExample();
    basicExample.setNameLike("nameLike");

    Specification<Object> specification1 = WhereBuilder.buildSpec(basicExample);
    Predicate predicate1 = specification1.toPredicate(root, criteriaQuery, criteriaBuilder);
    Assertions.assertNotNull(predicate1);

    mockLogin();
    Specification<Object> specification2 = WhereBuilder.buildSpecWithDept(basicExample);
    Assertions.assertNotNull(specification2);
  }

  private void mockLogin() {
    TestRealm realm = new TestRealm();
    DefaultSecurityManager dsm = new DefaultSecurityManager();
    dsm.setRealm(realm);
    //主体提交认证请求
    SecurityUtils.setSecurityManager(dsm);
    Subject sub = SecurityUtils.getSubject();
    //认证
    sub.login(new EmptyToken());
  }
}
