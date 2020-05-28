import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  constructor(private router: Router, private toastrService:ToastrService) { }

  ngOnInit(): void {
  }

  onLogout() {
    console.log('logging out');
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
    this.toastrService.success('logged out successfully');
  }

  getCurrentRoute(): string {
    return this.router.url;
  }

}
