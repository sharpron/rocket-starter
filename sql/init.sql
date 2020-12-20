use swt_gateway;
insert into sys_dept values (1, 'admin', sysdate(), 'admin', sysdate(), '中心', 1, '/', null);

insert into sys_user values (1, 'admin', sysdate(), 'admin', sysdate(), 0, 'sharprons@gmail.com', 0, '18281697060',
                             'b565ed208c19fa7f23227806709c15cef5daa2dc625da615870bc7ef6429e557',
                             '4711b98b-9950-4474-a783-875bdc075c37', 'admin', 1);



insert into sys_menu values (1, 'admin', sysdate(), 'admin', sysdate(), 'Layout', false, null, 1, null, 'sys', null, '系统管理');
insert into sys_menu values (default, 'admin', sysdate(), 'admin', sysdate(), 'views/system/user/index', false, null, 1, 1, 'user', null, '用户管理');
insert into sys_menu values (default, 'admin', sysdate(), 'admin', sysdate(), 'views/system/role/index', false, null, 2, 1, 'user', null, '角色管理');
insert into sys_menu values (default, 'admin', sysdate(), 'admin', sysdate(), 'views/system/menu/index', false, null, 3, 1, 'user', null, '菜单管理');
insert into sys_menu values (default, 'admin', sysdate(), 'admin', sysdate(), 'views/system/dept/index', false, null, 4, 1, 'user', null, '部门管理');

insert into sys_role values (default, 'admin', sysdate(), 'admin', sysdate(), '超级管理员，所有权限，不能被其它用户使用', false, '超级管理员', 1);
insert into sys_role_menu(role_id, menu_id) values (1, 1);
insert into sys_role_menu(role_id, menu_id) values (1, 2);
insert into sys_role_menu(role_id, menu_id) values (1, 3);
insert into sys_role_menu(role_id, menu_id) values (1, 4);
insert into sys_role_menu(role_id, menu_id) values (1, 5);

insert into sys_user_role(user_id, role_id) values (1, 1);

