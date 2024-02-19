package enums

import (
	"database/sql/driver"
	"errors"
	"fmt"
	"math"
)

type AdministrationRole byte

const (
	Dean              AdministrationRole = 0
	EducationalDeputy AdministrationRole = 1
	AcademicDeputy    AdministrationRole = 2
	None              AdministrationRole = math.MaxUint8
)

func (role AdministrationRole) Value() (driver.Value, error) {
	switch role {
	case Dean:
		return "Dean", nil
	case EducationalDeputy:
		return "EducationalDeputy", nil
	case AcademicDeputy:
		return "AcademicDeputy", nil
	default:
		return nil, errors.New("unknown value")
	}
}

func getAdministrationRoleByString(val string) (AdministrationRole, error) {
	switch val {
	case "Dean":
		return Dean, nil
	case "EducationalDeputy":
		return EducationalDeputy, nil
	case "AcademicDeputy":
		return AcademicDeputy, nil
	default:
		return None, errors.New("unknown name")
	}
}

func getAdministrationRoleByByte(val byte) (AdministrationRole, error) {
	switch val {
	case byte(Dean):
		return Dean, nil
	case byte(EducationalDeputy):
		return EducationalDeputy, nil
	case byte(AcademicDeputy):
		return AcademicDeputy, nil
	default:
		return None, errors.New("unknown name")
	}
}

func (role AdministrationRole) String() string {
	switch role {
	case Dean:
		return "Dean"
	case EducationalDeputy:
		return "EducationalDeputy"
	case AcademicDeputy:
		return "AcademicDeputy"
	}
	return ""
}

func (role *AdministrationRole) Scan(src interface{}) error {
	switch src := src.(type) {
	case nil:
		return nil
	case string:
		roleStr, err := getAdministrationRoleByString(src)
		if err == nil {
			*role = roleStr
			return nil
		}
		return fmt.Errorf("scan: %v", err)
	case []byte:
		roleStr, err := getAdministrationRoleByString(string(src))
		if err == nil {
			*role = roleStr
			return nil
		}
		return fmt.Errorf("scan: %v", err)

	case byte:
		roleByte, err := getAdministrationRoleByByte(src)
		if err == nil {
			*role = roleByte
			return nil
		}
		return fmt.Errorf("scan: %v", err)
	default:
		return fmt.Errorf("scan: unable to scan type %T into AdministrationRole", src)
	}

}
