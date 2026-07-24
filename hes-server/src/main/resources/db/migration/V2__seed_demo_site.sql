INSERT INTO site (site_code, name, timezone)
SELECT 'SITE-DEMO-001', 'Demo Shenzhen Residence', 'Asia/Shanghai'
WHERE NOT EXISTS (SELECT 1 FROM site WHERE site_code = 'SITE-DEMO-001');
