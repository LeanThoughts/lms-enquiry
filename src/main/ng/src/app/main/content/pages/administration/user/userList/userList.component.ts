import {Component, Input, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {BehaviorSubject} from 'rxjs';
import {fuseAnimations} from '@fuse/animations';
import {UserModel} from '../../../../model/user.model';
import {UserService} from '../user.service';

@Component({
  selector: 'fuse-user-list',
  templateUrl: './userList.component.html',
  styleUrls: ['./userList.component.scss'],
  animations: fuseAnimations,
  encapsulation: ViewEncapsulation.None
})
export class UserListComponent implements OnInit {


  dataSource: MatTableDataSource<any>;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;



//   @Input()
//   set usersList(usersList: UserModel[]) {
// //    this.dataSource = new MatTableDataSource(usersList);
//     this._service.usersCast.subscribe(users=> {
//       this.users = users;
//       this.dataSource = new MatTableDataSource(this.users);
//       this.dataSource.sort = this.sort;
//       this.dataSource.paginator = this.paginator;
//     });
//     this.dataSource.sort = this.sort
//     this.dataSource.paginator = this.paginator;
//   }



  users: UserModel[];

  selectedUser: UserModel;
  pageSizeOptions: number[] = [10, 25, 50, 100];

  displayedColumns = [
    'firstName', 'lastName', 'email', 'role', 'sapBPNumber', 'riskPortalDisplayOnlyAccess'
  ];

  constructor(private _service: UserService) {
      this._service.getUsers();
  }

  ngOnInit(): void {
    this._service.getUsers();

    this.users = new UserDataSource(this._service).connect().getValue();

    this.dataSource = new MatTableDataSource(this.users);

    this._service.usersCast.subscribe(users=> {
      this.users = users;
      this.dataSource = new MatTableDataSource(this.users);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    });

    //this.dataSource.sort = this.sort;


  }


  onSelect(user: UserModel): void {
    this.selectedUser = user;
    this._service.selectedUser.next(user);
  }
}

export class UserDataSource extends MatTableDataSource<UserModel> {
  constructor(private _service: UserService) {
    super();
  }

  connect(): BehaviorSubject<UserModel[]> {
    return this._service.users;
  }

  disconnect(): void {
  }
}
