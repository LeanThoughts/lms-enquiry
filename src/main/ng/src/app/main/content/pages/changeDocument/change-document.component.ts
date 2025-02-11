import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { ChangeDocumentService } from './change-document.service';

@Component({
    selector: 'app-change-document',
    templateUrl: './change-document.component.html',
    styleUrls: ['./change-document.component.scss']
})
export class ChangeDocumentComponent implements OnInit {

    changeDocumentsForm: FormGroup;

    changeDocuments: any;
    page: any = {
        totalElements: 0
    };

    constructor(_formBuilder: FormBuilder, private _service: ChangeDocumentService) {
        this.changeDocumentsForm = _formBuilder.group({
            processName: new FormControl(''),
            loanContractId: new FormControl(''),
            dateFrom: new FormControl(null),
            dateTo: new FormControl(null)
        });
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
    }

    /**
     * searchChangeDocuments()
     */
    searchChangeDocuments(): void {
        const formValue = this.changeDocumentsForm.value;
        this._service.fetchChangeDocuments(formValue.processName, formValue.loanContractId, formValue.dateFrom, formValue.dateTo, 0, 10).subscribe(data => {
            if (data !== null) {
                this.changeDocuments = data.content;
                this.page = data.pageable;
                this.page.totalElements = data.totalElements;
            }
        });
    }
}
