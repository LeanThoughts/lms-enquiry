import { Component, OnInit, Input } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
import { PageEvent, MatTableDataSource } from '@angular/material';
import { ChangeDocumentService } from '../change-document.service';

@Component({
    selector: 'app-change-document-list',
    templateUrl: './change-document-list.component.html',
    styleUrls: ['./change-document-list.component.scss'],
    animations: fuseAnimations
})
export class ChangeDocumentListComponent implements OnInit {

    dataSource: MatTableDataSource<any>;
    searchParameters: any;

    @Input()
    set changeDocuments(changeDocuments: any) {
        this.dataSource = new MatTableDataSource(changeDocuments);
    }

    @Input()
    page: any = {
        totalElements: 0
    };

    /**
     * Capture changes in the search parameters
     */
    @Input()
    set formValues(formValues: any) {
        this.searchParameters = formValues;
    }

    changeDocumentColumns: string[] = [
        'date', 'loanContractId', 'userName', 'action','businessProcessName', 'subProcessName',
    ];

    pageSizeOptions: number[] = [10, 25, 50, 100];

    itemColumns: string[] = [
        'itemNo', 'entityDescription', 'entityName', 'attributeName', 'tableKey', 'oldValue', 'newValue'
    ];

    selectedDocument: any;
    selectedItem: any;

    constructor(private _service: ChangeDocumentService) {
    }

    /**
     * ngOnInit()
     */
    ngOnInit(): void {
        this.dataSource = new MatTableDataSource(this.changeDocuments);
    }

    /**
     * onDocumentSelect()
     * @param changeDocument: any
     */
    onDocumentSelect(changeDocument: any): void {
        this.selectedDocument = changeDocument;
    }

    /**
     * onItemSelect()
     * @param documentItem: any
     */
    onItemSelect(documentItem: any): void {
        this.selectedItem = documentItem;
    }

    /**
     * pageOptionsChanged()
     * @param event: PageEvent
     */
    pageOptionsChanged(event: PageEvent): void {
        this._service.fetchChangeDocuments(this.searchParameters.processName, this.searchParameters.loanContractId,
            this.searchParameters.dateFrom,
            this.searchParameters.dateTo,
            event.pageIndex, event.pageSize).subscribe(data => {
                //
                this.dataSource = data.content;
                this.page = data.pageable;
                this.page.totalElements = data.totalElements;
                this.selectedDocument = undefined;
            });
    }
}
