import {Component, Input, OnInit, ViewChild} from '@angular/core';
import Any = jasmine.Any;
import {UserService} from "../user.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {fuseAnimations} from "../../../../../../../@fuse/animations/index";
import {UserModel} from "../../../../model/user.model";
import {MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {EnquiryApplicationModel} from "../../../../model/enquiryApplication.model";
import {BehaviorSubject} from "rxjs/Rx";

@Component({
  selector: 'app-user-mgmt',
  templateUrl: './user-mgmt.component.html',
  styleUrls: ['./user-mgmt.component.scss'],
  animations: fuseAnimations
})
export class UserMgmtComponent implements OnInit {


  dataSource: MatTableDataSource<any>;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  @Input()
  set userList(userList: UserModel[]) {
    this.dataSource = new MatTableDataSource(userList);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }


  selectedUser: UserModel;


  roleTypes: Array<any>;
  userSearchForm: FormGroup;
  expandPanel = true;

  pageSizeOptions: number[] = [10, 25, 50, 100];


  displayedColumns = [
    'firstName', 'lastName', 'email', 'role', 'sapBPNumber', 'riskPortalDisplayOnlyAccess'
  ];

  selectUser: UserModel;


  constructor( _route: ActivatedRoute, private _service: UserService, _formBuilder: FormBuilder,
               private _router: Router) {

    this.userSearchForm = _formBuilder.group({
      firstName: [],
      lastName: [],
      email: [],
      role: []

    });
    this.roleTypes = _route.snapshot.data.routeResolvedData[0]._embedded.userRoles;

    //this._service.selectedUser = new BehaviorSubject(new UserModel({}));
    this._service.selectedUser.next(new UserModel({}));


  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.userList);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;


  }


  /**
   * searchUsers()
   */
  searchUsers(): void {
    const searchForm = this.userSearchForm.value;

    let searchParameters: Array<string> = [searchForm.firstName,
      searchForm.lastName,
      searchForm.email,
      searchForm.role];


    this._service.searchUsers(searchParameters).subscribe((result) => {
      const users = new Array<UserModel>();

      result.map(userModel => {
        users.push(new UserModel(userModel));
      });
      console.log("Users : " + users.values());
      this.userList = users;
      this.expandPanel = true;
       this._service.selectedUser.next(new UserModel({}));
    });
  }



  onSelect(user: UserModel): void {
    this.selectedUser = user;
    this._service.selectedUser.next(user);
  }

}
