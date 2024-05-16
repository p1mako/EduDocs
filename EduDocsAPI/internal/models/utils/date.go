package utils

import "time"

type Date struct {
	time.Time
}

func (t *Date) UnmarshalJSON(b []byte) (err error) {
	date, err := time.Parse(`"2006-01-02T15:04"`, string(b))
	if err != nil {
		return err
	}
	t.Time = date
	return
}
