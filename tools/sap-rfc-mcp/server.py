"""
sap-rfc-mcp — a small MCP server that reads DDIC definitions and table data
from an SAP ECC system over RFC (SAP NetWeaver RFC SDK + pyrfc).

Tools
  get_table_definition(name)        DDIF_FIELDINFO_GET: fields of a table / structure / view
  get_data_element(name)            DDIF_DTEL_GET: data element with its domain
  get_domain_values(domain)         DD_DOMVALUES_GET: fixed values of a domain
  read_table(table, where, fields)  RFC_READ_TABLE: rows of a table (max 512 bytes per row)
  search_tables(pattern)            DD02L/DD02T: tables whose name matches a pattern
  get_function_interface(name)      RFC_GET_FUNCTION_INTERFACE: parameters of a function module
  ping()                            connection + system info

Connection parameters come from the environment (set them in claude_desktop_config.json):
  SAP_ASHOST   application server, e.g. 192.168.1.205
  SAP_SYSNR    instance number, e.g. 00
  SAP_CLIENT   client, e.g. 360
  SAP_USER     user
  SAP_PASSWD   password
  SAP_LANG     logon language (default EN)
  SAP_ROUTER   optional SAProuter string, e.g. /H/202.160.171.147
"""
from __future__ import annotations

import os
from typing import Any

from mcp.server.fastmcp import FastMCP
from pyrfc import Connection

mcp = FastMCP("sap-rfc")


def _params() -> dict[str, str]:
    p = {
        "ashost": os.environ["SAP_ASHOST"],
        "sysnr": os.environ.get("SAP_SYSNR", "00"),
        "client": os.environ["SAP_CLIENT"],
        "user": os.environ["SAP_USER"],
        "passwd": os.environ["SAP_PASSWD"],
        "lang": os.environ.get("SAP_LANG", "EN"),
    }
    if os.environ.get("SAP_ROUTER"):
        p["saprouter"] = os.environ["SAP_ROUTER"]
    return p


def _call(fm: str, **kwargs: Any) -> dict[str, Any]:
    """One RFC call per tool call: no long-lived connection to time out."""
    with Connection(**_params()) as conn:
        return conn.call(fm, **kwargs)


@mcp.tool()
def ping() -> dict[str, Any]:
    """Checks the RFC connection and returns system id, client, host and release."""
    with Connection(**_params()) as conn:
        info = conn.get_connection_attributes()
    return {k: info[k] for k in ("sysId", "client", "partnerHost", "partnerRel", "user") if k in info}


@mcp.tool()
def get_table_definition(name: str) -> list[dict[str, Any]]:
    """Fields of a DDIC table, structure or view (SE11 'Fields' tab): position, name, key flag,
    data element, domain, type, length, decimals, check table and description."""
    r = _call("DDIF_FIELDINFO_GET", TABNAME=name.upper(), LANGU="E", ALL_TYPES="X")
    fields = []
    for f in r["DFIES_TAB"]:
        fields.append({
            "position": int(f["POSITION"]),
            "field": f["FIELDNAME"],
            "key": f["KEYFLAG"] == "X",
            "dataElement": f["ROLLNAME"],
            "domain": f["DOMNAME"],
            "type": f["DATATYPE"],
            "length": int(f["LENG"]),
            "decimals": int(f["DECIMALS"]),
            "checkTable": f["CHECKTABLE"],
            "description": f["FIELDTEXT"],
            "include": f.get("PRECFIELD", ""),
        })
    return fields


@mcp.tool()
def get_data_element(name: str) -> dict[str, Any]:
    """A data element: its domain, type, length and the field labels."""
    r = _call("DDIF_DTEL_GET", NAME=name.upper(), LANGU="E")
    d = r["DD04V_WA"]
    return {
        "dataElement": d["ROLLNAME"],
        "domain": d["DOMNAME"],
        "type": d["DATATYPE"],
        "length": int(d["LENG"]),
        "decimals": int(d["DECIMALS"]),
        "shortText": d["DDTEXT"],
        "labels": {"short": d["SCRTEXT_S"], "medium": d["SCRTEXT_M"], "long": d["SCRTEXT_L"], "heading": d["REPTEXT"]},
    }


@mcp.tool()
def get_domain_values(domain: str) -> list[dict[str, str]]:
    """Fixed values of a domain (code and text), e.g. ZPROJ_VAL_BY -> 1 Credit Team, 2 Valuer."""
    r = _call("DD_DOMVALUES_GET", DOMNAME=domain.upper(), TEXT="X", LANGU="E")
    return [{"code": v["DOMVALUE_L"], "text": v["DDTEXT"]} for v in r["DD07V_TAB"]]


@mcp.tool()
def read_table(table: str, where: str = "", fields: str = "", max_rows: int = 100) -> dict[str, Any]:
    """Rows of a table through RFC_READ_TABLE. `where` is an ABAP WHERE clause without the keyword
    (e.g. "BUKRS = 'PFS' AND RANL = '0000020000231'"), `fields` a comma-separated list (empty = all).
    RFC_READ_TABLE limits a row to 512 bytes: for wide tables pass the fields you need."""
    kwargs: dict[str, Any] = {
        "QUERY_TABLE": table.upper(),
        "DELIMITER": "|",
        "ROWCOUNT": max(1, min(int(max_rows), 10000)),
    }
    if where.strip():
        # OPTIONS lines are 72 characters: split a long clause at spaces
        words, lines, current = where.split(), [], ""
        for w in words:
            if len(current) + len(w) + 1 > 72:
                lines.append(current.strip())
                current = ""
            current += w + " "
        lines.append(current.strip())
        kwargs["OPTIONS"] = [{"TEXT": line} for line in lines if line]
    if fields.strip():
        kwargs["FIELDS"] = [{"FIELDNAME": f.strip().upper()} for f in fields.split(",") if f.strip()]
    r = _call("RFC_READ_TABLE", **kwargs)
    names = [f["FIELDNAME"] for f in r["FIELDS"]]
    rows = []
    for line in r["DATA"]:
        values = line["WA"].split("|")
        rows.append({n: values[i].rstrip() if i < len(values) else "" for i, n in enumerate(names)})
    return {"table": table.upper(), "fields": names, "rowCount": len(rows), "rows": rows}


@mcp.tool()
def search_tables(pattern: str, max_rows: int = 50) -> list[dict[str, str]]:
    """Tables and structures whose name matches a pattern (SQL LIKE, use % as wildcard, e.g. ZCOL_%)."""
    tabs = read_table("DD02L", where=f"TABNAME LIKE '{pattern.upper()}' AND AS4LOCAL = 'A'",
                      fields="TABNAME,TABCLASS", max_rows=max_rows)
    texts = read_table("DD02T", where=f"TABNAME LIKE '{pattern.upper()}' AND DDLANGUAGE = 'E'",
                       fields="TABNAME,DDTEXT", max_rows=max_rows)
    text_of = {t["TABNAME"]: t["DDTEXT"] for t in texts["rows"]}
    return [{"table": t["TABNAME"], "class": t["TABCLASS"], "description": text_of.get(t["TABNAME"], "")}
            for t in tabs["rows"]]


@mcp.tool()
def get_function_interface(name: str) -> list[dict[str, str]]:
    """Parameters of a function module: kind (I import, E export, C changing, T table, X exception), name,
    type, optional flag and default."""
    r = _call("RFC_GET_FUNCTION_INTERFACE", FUNCNAME=name.upper(), LANGUAGE="E")
    return [{"kind": p["PARAMCLASS"], "name": p["PARAMETER"], "type": p["TABNAME"] or p["FIELDNAME"],
             "optional": p["OPTIONAL"] == "X", "default": p["DEFAULT"], "text": p["PARAMTEXT"]}
            for p in r["PARAMS"]]


if __name__ == "__main__":
    mcp.run()
