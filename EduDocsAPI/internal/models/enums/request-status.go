package enums

import (
	"database/sql/driver"
	"errors"
	"fmt"
	"math"
)

type RequestStatus byte

const (
	Sent           RequestStatus = 0
	BeingProcessed RequestStatus = 1
	CanBeTaken     RequestStatus = 2
	Received       RequestStatus = 3
	Declined       RequestStatus = 4
	Removed        RequestStatus = 5
)

func (status RequestStatus) Value() (driver.Value, error) {
	switch status {
	case Sent:
		return "Sent", nil
	case BeingProcessed:
		return "BeingProcessed", nil
	case CanBeTaken:
		return "CanBeTaken", nil
	case Received:
		return "Received", nil
	case Declined:
		return "Declined", nil
	case Removed:
		return "Removed", nil
	default:
		return nil, errors.New("unknown value")
	}
}

func getRequestStatusByString(val string) (RequestStatus, error) {
	switch val {
	case "Sent":
		return Sent, nil
	case "BeingProcessed":
		return BeingProcessed, nil
	case "CanBeTaken":
		return CanBeTaken, nil
	case "Received":
		return Received, nil
	case "Declined":
		return Declined, nil
	case "Removed":
		return Received, nil
	default:
		return math.MaxUint8, errors.New("unknown name")
	}
}

func getRequestStatusByByte(val byte) (RequestStatus, error) {
	switch val {
	case byte(Sent):
		return Sent, nil
	case byte(BeingProcessed):
		return BeingProcessed, nil
	case byte(CanBeTaken):
		return CanBeTaken, nil
	case byte(Received):
		return Received, nil
	case byte(Declined):
		return Declined, nil
	case byte(Removed):
		return Removed, nil
	default:
		return math.MaxUint8, errors.New("unknown name")
	}
}

func (status RequestStatus) String() string {
	switch status {
	case Sent:
		return "Sent"
	case BeingProcessed:
		return "BeingProcessed"
	case CanBeTaken:
		return "CanBeTaken"
	case Received:
		return "Received"
	case Declined:
		return "Declined"
	case Removed:
		return "Removed"
	}
	return ""
}

func (status *RequestStatus) Scan(src interface{}) error {
	switch src := src.(type) {
	case nil:
		return nil
	case string:
		statusStr, err := getRequestStatusByString(src)
		if err == nil {
			*status = statusStr
			return nil
		}
		return fmt.Errorf("scan: %v", err)
	case []byte:
		statusStr, err := getRequestStatusByString(string(src))
		if err == nil {
			*status = statusStr
			return nil
		}
		return fmt.Errorf("scan: %v", err)

	case byte:
		statusByte, err := getRequestStatusByByte(src)
		if err == nil {
			*status = statusByte
			return nil
		}
		return fmt.Errorf("scan: %v", err)
	default:
		return fmt.Errorf("scan: unable to scan type %T into AdministrationRole", src)
	}

}
