# sap-rfc-mcp

MCP server that reads DDIC definitions (tables, structures, data elements, domain values),
table data and function-module interfaces from SAP ECC over RFC. Complements the ADT-based
`mcp-abap-adt` server (source code), whose DDIC services do not exist on NetWeaver 7.31.

## 1. SAP NetWeaver RFC SDK (once, needs an S-user)

1. SAP for Me → Software Downloads → search **SAP NW RFC SDK 7.50** → platform
   **MACOS ON ARM 64-BIT** (Apple Silicon). Download the `.zip`.
2. Unzip to `~/nwrfcsdk` so that `~/nwrfcsdk/lib/libsapnwrfc.dylib` exists.
3. macOS blocks unsigned dylibs downloaded from the internet; clear the quarantine flag:
   ```bash
   xattr -dr com.apple.quarantine ~/nwrfcsdk
   ```
4. Add to `~/.zshrc`:
   ```bash
   export SAPNWRFC_HOME=$HOME/nwrfcsdk
   export DYLD_LIBRARY_PATH=$SAPNWRFC_HOME/lib:$DYLD_LIBRARY_PATH
   ```
   and open a new terminal.

## 2. Python environment

```bash
cd ~/IdeaProjects/lms-enquiry/tools/sap-rfc-mcp
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python -c "import pyrfc; print(pyrfc.__version__)"
```

If `import pyrfc` fails with *"Library not loaded ... libsapnwrfc.dylib"*, the SDK's rpath is not
set; run once (from the pyrfc installation notes for macOS):
```bash
cd $SAPNWRFC_HOME/lib
for f in *.dylib; do install_name_tool -add_rpath $SAPNWRFC_HOME/lib $f 2>/dev/null; done
codesign -f -s - *.dylib
```

## 3. Test the connection (VPN on)

```bash
SAP_ASHOST=192.168.1.205 SAP_SYSNR=00 SAP_CLIENT=360 SAP_USER=SAJEEV SAP_PASSWD=... \
python -c "import server; print(server.ping())"
```
If the connection times out, the VPN does not route port 3300 (instance 00); add
`SAP_ROUTER=/H/202.160.171.147` (the SAProuter string from SAP Logon) and try again.

## 4. Register in the Claude desktop app

Settings → Developer → Edit Config, add beside `sap-ecd`:

```json
"sap-rfc": {
  "command": "/Users/admin/IdeaProjects/lms-enquiry/tools/sap-rfc-mcp/.venv/bin/python",
  "args": ["/Users/admin/IdeaProjects/lms-enquiry/tools/sap-rfc-mcp/server.py"],
  "env": {
    "SAPNWRFC_HOME": "/Users/admin/nwrfcsdk",
    "DYLD_LIBRARY_PATH": "/Users/admin/nwrfcsdk/lib",
    "SAP_ASHOST": "192.168.1.205",
    "SAP_SYSNR": "00",
    "SAP_CLIENT": "360",
    "SAP_USER": "SAJEEV",
    "SAP_PASSWD": "<password>",
    "SAP_LANG": "EN"
  }
}
```
Quit and reopen Claude. Tools: `ping`, `get_table_definition`, `get_data_element`,
`get_domain_values`, `read_table`, `search_tables`, `get_function_interface`.

## Authorizations

The user needs `S_RFC` for function groups `SDIFRUNTIME` (DDIF_*), `SDDO` / `SDDI`
(domain values), `SDTX` (RFC_READ_TABLE), `RFC1` (RFC_GET_FUNCTION_INTERFACE) and
`S_TABU_DIS` / `S_TABU_NAM` for the tables read. A developer user has all of these.
