import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router, private toastrService:ToastrService) {
  }

  ngOnInit(): void {

  }

  onLogout() {
    console.log('logging out');
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
    this.toastrService.success('logged out successfully')
  }

  getCurrentRoute(): string {
    return this.router.url;
  }
}
