--
-- This file is part of QueenBee Project.
--
-- QueenBee Project is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- QueenBee Project is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with QueenBee Project.  If not, see <http://www.gnu.org/licenses/>.
--

CREATE TABLE TB_KEYSTORE(
	CL_NAME				VARCHAR(512),
	CL_CREATIONDATE		TIMESTAMP NOT NULL,
	CONSTRAINT CC_KEYSTORE_PK
	PRIMARY KEY (CL_NAME)
);

REVOKE ALL
ON TABLE TB_KEYSTORE
FROM PUBLIC;

COMMENT ON TABLE TB_KEYSTORE
IS 'Keystore registry table';

COMMENT ON COLUMN TB_KEYSTORE.CL_NAME
IS 'Keystore identifier name';

COMMENT ON COLUMN TB_KEYSTORE.CL_CREATIONDATE
IS 'Date when keystore was created';

COMMENT ON CONSTRAINT CC_KEYSTORE_PK
ON TB_KEYSTORE
IS 'Primary key of keystore';