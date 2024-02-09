package enums

type RequestStatus byte

const (
	Sent           RequestStatus = 0
	BeingProcessed RequestStatus = 1
	CanBeTaken     RequestStatus = 2
	Received       RequestStatus = 3
	Declined       RequestStatus = 4
	Removed        RequestStatus = 5
)
