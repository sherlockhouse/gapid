// Copyright (C) 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package pack

import (
	"fmt"

	"github.com/google/gapid/core/fault"
)

const (
	// Magic is the file magic that prefixes all pack files.
	Magic = "protopack"

	// ErrIncorrectMagic is the error returned when the file header is not matched.
	ErrIncorrectMagic = fault.Const("Incorrect pack magic header")

	// VersionMajor is the curent major version the package writes.
	VersionMajor = 1
	// VersionMinor is the current minor version the package writes.
	VersionMinor = 0

	initalBufferSize = 4096
	maxVarintSize    = 10
	specialSection   = 0
)

type (
	// ErrUnknownVersion is the error returned when the header version is one this
	// package cannot handle.
	ErrUnknownVersion struct{ Version *Version }
)

func (e ErrUnknownVersion) Error() string {
	return fmt.Sprintf("Unknown pack file version: %+v", e.Version)
}

var (
	magicBytes = []byte(Magic)
	version    = Version{
		Major: VersionMajor,
		Minor: VersionMinor,
	}
)
