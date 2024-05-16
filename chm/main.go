package main

import (
	"math"
)

const (
	a = 1
	r = 0.5
	h = 0.001
	m = 2 / h
	n = 2 / a / (r * h)
)

var utable [n][m]float64

func u(x, t float64) float64 {
	return math.Sin(a*t - x)
}

func f(u1, u2, u3 float64) float64 {
	return 0
}

func main() {
	for i := 0; i < n; i++ {
		utable[0][i] = 0
	}
	var t float64
	for i := 0; i < n; i++ {
		utable[i][0] = math.Sin(a*t + 1)
		utable[i][m-1] = math.Sin(a*t - 1)
		for j := 0; j < m-2; j++ {
			utable[i][j+1] = f(utable[i-1][j], utable[i-1][j+1], utable[i-1][j+2])
		}
	}
}
