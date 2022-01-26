CREATE AGGREGATE FUNCTION group_concat_distinct
    (IN val VARCHAR(100), IN flag BOOLEAN, INOUT buffer VARCHAR(1000), INOUT counter INT)
        RETURNS VARCHAR(1000)
        CONTAINS SQL
        BEGIN ATOMIC
            IF FLAG THEN
                RETURN BUFFER;
            ELSE
                IF val IS NULL THEN RETURN NULL; END IF;
                IF buffer IS NULL THEN SET BUFFER = ''; END IF;
                IF counter IS NULL THEN SET COUNTER = 0; END IF;
                IF (LOCATE(val, buffer) = 0 OR counter = 0) THEN
                    IF counter > 0 THEN SET buffer = buffer || ','; END IF;
                    SET buffer = buffer + val;
                END IF;
                SET counter = counter + 1;
                RETURN NULL;
            END IF;
        END