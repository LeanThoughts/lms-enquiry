import { TAX_PERCENTAGE_REGEX } from "../../../common/common.regex";
import { ReferenceInterestRateService } from "../../reference-interest-rate/reference-interest-rate.service";
import { StageConfig } from "./generic-config.model";

// Values awaiting approval (workflow status 1) or marked for deletion (modification status 2) cannot be changed
const isLocked = (value: any): boolean => value?.workFlowStatusCode === 1 || value?.modificationStatus === 2;

const CASH_FLOW_IMPACT_MESSAGE = 'This will impact cash flows of existing loans in the system.';

// Reference Interest Rates - list and update dialog configurations
export const referenceInterestRateStageConfig: StageConfig = {
    service: ReferenceInterestRateService,

    list: {
        referenceInterestRateValues: {
            displayedColumns: [
                {name: 'referenceInterestRateCode', header: 'Interest Rate Type Code', type: 'text'},
                {name: 'referenceInterestRateDescription', header: 'Interest Rate Type Description', type: 'text'},
                {name: 'validFromDate', header: 'Valid From Date', type: 'date'},
                {name: 'interestRate', header: 'Interest Rate (%)', type: 'number'},
                {name: 'modificationStatusDescription', header: 'Modification Status', type: 'text'},
                {name: 'workFlowStatusDescription', header: 'Workflow Status', type: 'text'},
            ],
            fetchFunction: ReferenceInterestRateService.prototype.getReferenceInterestRates,
            createButton: true,
            updateButton: true,
            deleteButton: true,
            viewButton: true,
            deleteFunction: ReferenceInterestRateService.prototype.deleteReferenceInterestRate,
            deleteConfirmationMessage: 'Are you sure you want to delete this reference interest value? ' + CASH_FLOW_IMPACT_MESSAGE,
            deleteSuccessMessage: 'Reference interest value marked for deletion',
            disableUpdate: isLocked,
            disableDelete: isLocked,
            updateDialogWidth: '30rem',
            viewDialogWidth: '30rem',
        }
    },

    updateDialog: {
        referenceInterestRateValues: {
            searchString1ForCreate: 'referenceInterestRate',
            passSearchString1Via: 'Object',
            createDialogTitle: 'Add Reference Interest Value',
            updateDialogTitle: 'Update Reference Interest Value',
            viewDialogTitle: 'View Reference Interest Value',
            createFunction: ReferenceInterestRateService.prototype.saveReferenceInterestRate,
            updateFunction: ReferenceInterestRateService.prototype.updateReferenceInterestRate,
            createSuccessMessage: 'Reference interest value added successfully',
            updateSuccessMessage: 'Reference interest value updated successfully',
            submitConfirmationMessage: 'Are you sure you want to save this reference interest value? ' + CASH_FLOW_IMPACT_MESSAGE,
            fieldsConfig: [
                { row: 1, span: 12, name: 'validFromDate', label: 'Valid From Date', type: 'date', required: true, readOnlyOnUpdate: true },
                { row: 2, span: 12, name: 'interestRate', label: 'Interest Rate (%)', type: 'text', required: true, maxLength: 6, 
                    pattern: TAX_PERCENTAGE_REGEX },
            ]
        }
    },
};
