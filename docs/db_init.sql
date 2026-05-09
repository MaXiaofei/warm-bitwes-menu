-- ============================================================
-- warm-bitwes-menu：V1 全量建库脚本（按全局规范补齐）
--
-- 规范对齐：
-- - 每张业务表都必须包含 remark（VARCHAR(512)）
-- - 审计时间字段精确到毫秒：DATETIME(3)
--
-- 执行方式（示例）：
-- mysql -uroot -p -h127.0.0.1 -e "SOURCE /path/to/docs/db_init.sql"
--
-- ⚠️ 警告：脚本会清空并重建以下业务表（开发环境可用）
-- ============================================================

SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLE IF EXISTS prep_item;
-- DROP TABLE IF EXISTS session_review;
-- DROP TABLE IF EXISTS session_retrospective;
-- DROP TABLE IF EXISTS cook_event;
-- DROP TABLE IF EXISTS session_dish;
-- DROP TABLE IF EXISTS dish_ingredient;
-- DROP TABLE IF EXISTS dish_category;
-- DROP TABLE IF EXISTS dish_tag;
-- DROP TABLE IF EXISTS dish_cuisine;

-- DROP TABLE IF EXISTS menu_template_dish;
-- DROP TABLE IF EXISTS cooking_session;

-- DROP TABLE IF EXISTS menu_template;
-- DROP TABLE IF EXISTS dish;
-- DROP TABLE IF EXISTS ingredient;

-- DROP TABLE IF EXISTS biz_dropdown_option;

-- DROP TABLE IF EXISTS user_role;
-- DROP TABLE IF EXISTS app_user;
-- DROP TABLE IF EXISTS role;

-- DROP TABLE IF EXISTS cuisine;
-- DROP TABLE IF EXISTS tag;
-- DROP TABLE IF EXISTS category;

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- role / user / user_role
-- ----------------------------
CREATE TABLE role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
  name VARCHAR(64) NOT NULL COMMENT '角色名（全局唯一），如 admin/prep/diner',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_role_name (name),
  KEY idx_role_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色';


CREATE TABLE app_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(64) NOT NULL COMMENT '登录名（全局唯一）',
  password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希（后续接入安全组件）',
  nickname VARCHAR(64) DEFAULT NULL COMMENT '昵称',
  phone VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_app_user_username (username),
  UNIQUE KEY uk_app_user_phone (phone),
  KEY idx_app_user_status (status),
  KEY idx_app_user_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';


CREATE TABLE user_role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_user_role_user_role (user_id, role_id),
  KEY idx_user_role_role_id (role_id),
  CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES app_user (id),
  CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色关联';

-- ----------------------------
-- permission_point / role_permission / role_scope
-- ----------------------------
CREATE TABLE permission_point (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限点ID',
  code VARCHAR(64) NOT NULL COMMENT '权限编码',
  name VARCHAR(128) NOT NULL COMMENT '权限名称',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_permission_point_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限点';

CREATE TABLE role_permission (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  permission_id BIGINT NOT NULL COMMENT '权限点ID',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_role_permission (role_id, permission_id),
  KEY idx_role_permission_permission_id (permission_id),
  CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES role (id),
  CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permission_point (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色-权限点';

CREATE TABLE role_scope (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  client_type VARCHAR(32) NOT NULL COMMENT '端类型：admin/mini',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_role_scope (role_id, client_type),
  CONSTRAINT fk_role_scope_role FOREIGN KEY (role_id) REFERENCES role (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色-端范围';


-- ----------------------------
-- ingredient（热量 + GI）
-- ----------------------------
CREATE TABLE ingredient (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '食材ID',
  name VARCHAR(100) NOT NULL COMMENT '食材名称（全局唯一）',
  unit VARCHAR(32) NOT NULL COMMENT '展示单位（g/个/勺等）',
  calories_kcal_per_100g DECIMAL(10, 2) NOT NULL COMMENT '每100g热量(kcal)',
  gi_value DECIMAL(10, 2) NOT NULL COMMENT 'GI（升糖指数）',
  protein_g_per_100g DECIMAL(10, 2) DEFAULT NULL COMMENT '每100g蛋白质(g)',
  fat_g_per_100g DECIMAL(10, 2) DEFAULT NULL COMMENT '每100g脂肪(g)',
  carb_g_per_100g DECIMAL(10, 2) DEFAULT NULL COMMENT '每100g碳水(g)',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_ingredient_name (name),
  KEY idx_ingredient_name_created (name, created_at),
  KEY idx_ingredient_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食材';


-- ----------------------------
-- cuisine / tag / category
-- ----------------------------
CREATE TABLE cuisine (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜系ID',
  name VARCHAR(64) NOT NULL COMMENT '菜系名称（全局唯一），如鲁菜、川菜',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_cuisine_name (name),
  KEY idx_cuisine_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜系';


CREATE TABLE tag (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
  name VARCHAR(64) NOT NULL COMMENT '标签名称（全局唯一），如减脂餐、快手菜',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_tag_name (name),
  KEY idx_tag_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签';


CREATE TABLE category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
  name VARCHAR(64) NOT NULL COMMENT '分类名称（全局唯一），如热菜、凉菜、甜品',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_category_name (name),
  KEY idx_category_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类';


-- ----------------------------
-- dish
-- ----------------------------
CREATE TABLE dish (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜品ID',
  name VARCHAR(120) NOT NULL COMMENT '菜名',
  cover_url VARCHAR(255) DEFAULT NULL COMMENT '封面图URL',
  steps LONGTEXT DEFAULT NULL COMMENT '做法步骤',
  notes LONGTEXT DEFAULT NULL COMMENT '备注',
  duration_min INT DEFAULT NULL COMMENT '耗时（分钟）',
  difficulty TINYINT DEFAULT NULL COMMENT '难度（建议1-5）',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1上架 0下架',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0否 1是',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  KEY idx_dish_is_deleted (is_deleted),
  KEY idx_dish_status (status),
  KEY idx_dish_name (name),
  KEY idx_dish_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品';


-- ----------------------------
-- dish relations
-- ----------------------------
CREATE TABLE dish_ingredient (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  dish_id BIGINT NOT NULL COMMENT '菜品ID',
  ingredient_id BIGINT NOT NULL COMMENT '食材ID',
  amount_g DECIMAL(10, 2) NOT NULL COMMENT '用量（克，热量/GI计算口径）',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '同一菜品展示顺序',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注（如：腌制用/出锅前加）',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_di_dish_ingredient (dish_id, ingredient_id),
  KEY idx_di_dish_id (dish_id),
  KEY idx_di_ingredient_id (ingredient_id),
  CONSTRAINT fk_di_dish FOREIGN KEY (dish_id) REFERENCES dish (id),
  CONSTRAINT fk_di_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品-食材用量';


CREATE TABLE dish_cuisine (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  dish_id BIGINT NOT NULL COMMENT '菜品ID',
  cuisine_id BIGINT NOT NULL COMMENT '菜系ID',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '同一菜品多个菜系排序',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_dc_dish_cuisine (dish_id, cuisine_id),
  KEY idx_dc_dish_id (dish_id),
  KEY idx_dc_cuisine_id (cuisine_id),
  CONSTRAINT fk_dc_dish FOREIGN KEY (dish_id) REFERENCES dish (id),
  CONSTRAINT fk_dc_cuisine FOREIGN KEY (cuisine_id) REFERENCES cuisine (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品-菜系';


CREATE TABLE dish_tag (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  dish_id BIGINT NOT NULL COMMENT '菜品ID',
  tag_id BIGINT NOT NULL COMMENT '标签ID',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '同一菜品多个标签排序',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_dt_dish_tag (dish_id, tag_id),
  KEY idx_dt_dish_id (dish_id),
  KEY idx_dt_tag_id (tag_id),
  CONSTRAINT fk_dt_dish FOREIGN KEY (dish_id) REFERENCES dish (id),
  CONSTRAINT fk_dt_tag FOREIGN KEY (tag_id) REFERENCES tag (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品-标签';


CREATE TABLE dish_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  dish_id BIGINT NOT NULL COMMENT '菜品ID',
  category_id BIGINT NOT NULL COMMENT '分类ID',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '同一菜品多个分类排序',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_dcat_dish_category (dish_id, category_id),
  KEY idx_dcat_dish_id (dish_id),
  KEY idx_dcat_category_id (category_id),
  CONSTRAINT fk_dcat_dish FOREIGN KEY (dish_id) REFERENCES dish (id),
  CONSTRAINT fk_dcat_category FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜品-分类（多对多）';


-- ----------------------------
-- menu template
-- ----------------------------
CREATE TABLE menu_template (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单模板ID',
  name VARCHAR(120) NOT NULL COMMENT '模板名称',
  template_type TINYINT NOT NULL COMMENT '1一日三餐 2家宴菜单 3节日菜单 4自定义模板',
  scene VARCHAR(64) DEFAULT NULL COMMENT '场景（可选）',
  flavor VARCHAR(64) DEFAULT NULL COMMENT '口味（可选）',
  crowd VARCHAR(64) DEFAULT NULL COMMENT '人群（可选）',
  description LONGTEXT DEFAULT NULL COMMENT '说明',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
  created_by BIGINT DEFAULT NULL COMMENT '创建人（user_id）',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  KEY idx_menu_template_type (template_type),
  KEY idx_menu_template_status (status),
  KEY idx_menu_template_created_by (created_by),
  CONSTRAINT fk_menu_template_created_by FOREIGN KEY (created_by) REFERENCES app_user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单模板';


CREATE TABLE menu_template_dish (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  template_id BIGINT NOT NULL COMMENT '模板ID',
  dish_id BIGINT NOT NULL COMMENT '菜品ID',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '模板内菜品排序',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_mtd_template_dish (template_id, dish_id),
  KEY idx_mtd_template_id (template_id),
  KEY idx_mtd_dish_id (dish_id),
  CONSTRAINT fk_mtd_template FOREIGN KEY (template_id) REFERENCES menu_template (id),
  CONSTRAINT fk_mtd_dish FOREIGN KEY (dish_id) REFERENCES dish (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板-菜品';


-- ----------------------------
-- cooking session / scores / prep
-- ----------------------------
CREATE TABLE cooking_session (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID（一次做饭/菜单使用记录）',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  template_id BIGINT DEFAULT NULL COMMENT '模板ID（可为空：自定义选菜）',
  scene VARCHAR(64) DEFAULT NULL COMMENT '快照：场景',
  flavor VARCHAR(64) DEFAULT NULL COMMENT '快照：口味',
  crowd VARCHAR(64) DEFAULT NULL COMMENT '快照：人群',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1进行中 2已完成 0已取消',
  started_at DATETIME(3) DEFAULT NULL COMMENT '开始时间',
  ended_at DATETIME(3) DEFAULT NULL COMMENT '结束时间',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  KEY idx_cs_user_id (user_id),
  KEY idx_cs_template_id (template_id),
  KEY idx_cs_status (status),
  CONSTRAINT fk_cs_user FOREIGN KEY (user_id) REFERENCES app_user (id),
  CONSTRAINT fk_cs_template FOREIGN KEY (template_id) REFERENCES menu_template (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='烹饪会话';


CREATE TABLE session_dish (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  session_id BIGINT NOT NULL COMMENT '会话ID',
  dish_id BIGINT NOT NULL COMMENT '菜品ID',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '本次菜单排序',
  score_taste TINYINT DEFAULT NULL COMMENT '口味评分（可选，建议0-5）',
  score_difficulty TINYINT DEFAULT NULL COMMENT '难度评分（可选，建议0-5）',
  replay_intention TINYINT DEFAULT NULL COMMENT '复做意愿（可选，建议0-5）',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_sd_session_dish (session_id, dish_id),
  KEY idx_sd_session_id (session_id),
  KEY idx_sd_dish_id (dish_id),
  CONSTRAINT fk_sd_session FOREIGN KEY (session_id) REFERENCES cooking_session (id),
  CONSTRAINT fk_sd_dish FOREIGN KEY (dish_id) REFERENCES dish (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话-菜品（点评）';


CREATE TABLE cook_event (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '事件ID',
  session_id BIGINT NOT NULL COMMENT '会话ID',
  event_type VARCHAR(32) NOT NULL COMMENT '事件类型（START/STEP/PAUSE/RESUME/END等）',
  event_time DATETIME(3) NOT NULL COMMENT '事件发生时间',
  content VARCHAR(1000) DEFAULT NULL COMMENT '事件内容',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  KEY idx_ce_session_id (session_id),
  KEY idx_ce_session_time (session_id, event_time),
  KEY idx_ce_event_type (event_type),
  CONSTRAINT fk_ce_session FOREIGN KEY (session_id) REFERENCES cooking_session (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='烹饪过程事件';


CREATE TABLE session_retrospective (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '复盘ID',
  session_id BIGINT NOT NULL COMMENT '会话ID',
  summary VARCHAR(1000) DEFAULT NULL COMMENT '复盘总结',
  improvement VARCHAR(1000) DEFAULT NULL COMMENT '改进建议',
  retry_advice VARCHAR(1000) DEFAULT NULL COMMENT '复做建议',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_sr_session_id (session_id),
  KEY idx_sr_created_at (created_at),
  CONSTRAINT fk_sr_session FOREIGN KEY (session_id) REFERENCES cooking_session (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话复盘';


CREATE TABLE session_review (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点评ID',
  session_id BIGINT NOT NULL COMMENT '会话ID',
  dish_id BIGINT DEFAULT NULL COMMENT '菜品ID（可空：针对整场会话点评）',
  taste_score TINYINT DEFAULT NULL COMMENT '口味评分（1-5）',
  difficulty_score TINYINT DEFAULT NULL COMMENT '难度评分（1-5）',
  retry_intent VARCHAR(16) DEFAULT NULL COMMENT '复做意愿（YES/NO/LATER）',
  review_note VARCHAR(500) DEFAULT NULL COMMENT '点评备注',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  KEY idx_srev_session_id (session_id),
  KEY idx_srev_dish_id (dish_id),
  KEY idx_srev_created_at (created_at),
  CONSTRAINT fk_srev_session FOREIGN KEY (session_id) REFERENCES cooking_session (id),
  CONSTRAINT fk_srev_dish FOREIGN KEY (dish_id) REFERENCES dish (id),
  CONSTRAINT chk_srev_taste_score CHECK (taste_score IS NULL OR (taste_score BETWEEN 1 AND 5)),
  CONSTRAINT chk_srev_difficulty_score CHECK (difficulty_score IS NULL OR (difficulty_score BETWEEN 1 AND 5)),
  CONSTRAINT chk_srev_retry_intent CHECK (retry_intent IS NULL OR retry_intent IN ('YES', 'NO', 'LATER'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话点评';


CREATE TABLE prep_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '备菜项ID',
  session_id BIGINT NOT NULL COMMENT '会话ID',
  ingredient_id BIGINT NOT NULL COMMENT '食材ID',
  plan_amount_g DECIMAL(10, 2) NOT NULL COMMENT '计划用量（克）',
  actual_amount_g DECIMAL(10, 2) DEFAULT NULL COMMENT '实际用量（克，可选）',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1待采购 2已采购 3已备好 4已完成 0取消',
  is_shortage TINYINT NOT NULL DEFAULT 0 COMMENT '是否缺货：0否 1是',
  note VARCHAR(255) DEFAULT NULL COMMENT '附加说明（业务字段）',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注（全局规范字段）',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_pi_session_ingredient (session_id, ingredient_id),
  KEY idx_pi_session_id (session_id),
  KEY idx_pi_ingredient_id (ingredient_id),
  KEY idx_pi_is_shortage (is_shortage),
  CONSTRAINT fk_pi_session FOREIGN KEY (session_id) REFERENCES cooking_session (id),
  CONSTRAINT fk_pi_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='备菜项/采购清单';


-- ----------------------------
-- 下拉选项配置（管理端「下拉配置」等）
-- ----------------------------
CREATE TABLE biz_dropdown_option (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  category VARCHAR(64) NOT NULL COMMENT '分类编码，如 MENU_TEMPLATE_TYPE',
  option_code VARCHAR(64) NOT NULL COMMENT '选项编码（业务解析用）',
  option_label VARCHAR(128) NOT NULL COMMENT '展示文案',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
  enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0否 1是',
  remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  UNIQUE KEY uk_dd_cat_code (category, option_code),
  KEY idx_dd_category (category),
  KEY idx_dd_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='下拉选项配置';

-- 模板类型（与 menu_template.template_type 注释 1–4 一致）
INSERT INTO biz_dropdown_option (category, option_code, option_label, sort_order, enabled, remark) VALUES
('MENU_TEMPLATE_TYPE', '1', '一日三餐', 10, 1, NULL),
('MENU_TEMPLATE_TYPE', '2', '家宴菜单', 20, 1, NULL),
('MENU_TEMPLATE_TYPE', '3', '节日菜单', 30, 1, NULL),
('MENU_TEMPLATE_TYPE', '4', '自定义模板', 40, 1, NULL);
