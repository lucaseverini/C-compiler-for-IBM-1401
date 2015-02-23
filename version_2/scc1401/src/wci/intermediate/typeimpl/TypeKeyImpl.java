package wci.intermediate.typeimpl;

import wci.intermediate.TypeKey;

/**
 * <h1>TypeKeyImpl</h1>
 *
 * <p>Attribute keys for a Pascal type specification.</p>
 *
 * <p>Copyright (c) 2008 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public enum TypeKeyImpl implements TypeKey
{

    // Array
    ARRAY_INDEX_TYPE, ARRAY_ELEMENT_TYPE, ARRAY_ELEMENT_COUNT,

    // Record
    RECORD_SYMTAB
}
