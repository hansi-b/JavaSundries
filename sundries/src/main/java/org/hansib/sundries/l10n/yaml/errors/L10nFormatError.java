package org.hansib.sundries.l10n.yaml.errors;

public sealed interface L10nFormatError permits //
UnexpectedRootNode, UnknownEnum, DuplicateEnum, UnexpectedEnumNode, //
UnexpectedTextValueNode, UnknownEnumKey, DuplicateEnumValue //
{
	String description();
}
