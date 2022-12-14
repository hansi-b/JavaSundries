package org.hansib.sundries.l10n.yaml.errors;

public sealed interface L10nFormatError permits //
UnexpectedRootNode, ParseError, UnknownEnum, DuplicateEnum, UnexpectedEnumNode, //
UnexpectedTextValueNode, UnknownEnumKey, DuplicateEnumValue //
{
	String description();
}
