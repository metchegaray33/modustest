/**SUPPLIERS*/

INSERT INTO suppliers(id,name,address,contact)VALUES(1,'NETFLIX','BOUSTON STREET',222) ON DUPLICATE KEY UPDATE id = VALUES(id);
INSERT INTO suppliers(id,name,address,contact)VALUES(2,'AMAZON','CAROLILNA STREET',333) ON DUPLICATE KEY UPDATE id = VALUES(id);
INSERT INTO suppliers(id,name,address,contact)VALUES(3,'FLOW','SAN MARTIN STREET',444) ON DUPLICATE KEY UPDATE id = VALUES(id);
