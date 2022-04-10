CREATE TABLE public.oauth2api_scope (
	"role" varchar NOT NULL,
	"scope" varchar NOT NULL,
	CONSTRAINT oauth2api_scope_pk PRIMARY KEY (role, scope)
);

CREATE TABLE public.oauth2api_user (
	username varchar NOT NULL,
	"password" varchar NOT NULL,
	"role" varchar NOT NULL,
	CONSTRAINT oauth2api_user_pk PRIMARY KEY (username)
);

CREATE TABLE public.phone_contact (
	"name" varchar NOT NULL,
	phone varchar NOT NULL,
	id bigserial NOT NULL,
	CONSTRAINT phone_contact_pk PRIMARY KEY (id)
);

CREATE TABLE public.phone_contact_user (
	user_id varchar NOT NULL,
	phone_contact_id int8 NOT NULL,
	CONSTRAINT phone_contact_user_pk PRIMARY KEY (user_id, phone_contact_id)
);

INSERT INTO public.oauth2api_user (username,"password","role") VALUES
	 ('super_user','$2a$10$Ce1JN02E7MDSYtULG9AB6uqdNcGWlGK210.A5X.XVKiM1TsO/Ygp6','SUPER_USER'),
	 ('normal_user','$2a$10$Ce1JN02E7MDSYtULG9AB6uqdNcGWlGK210.A5X.XVKiM1TsO/Ygp6','NORMAL_USER');

INSERT INTO public.oauth2api_scope ("role","scope") VALUES
	 ('SUPER_USER','read'),
	 ('SUPER_USER','write'),
	 ('SUPER_USER','delete'),
	 ('NORMAL_USER','read'),
	 ('NORMAL_USER','write');

commit;

