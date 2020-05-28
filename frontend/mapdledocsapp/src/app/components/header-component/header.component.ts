import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router) {
  }

  ngOnInit(): void {

  }

  onLogout() {
    console.log('logging out');
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
  }

  getCurrentRoute(): string {
    return this.router.url;
  }
}
