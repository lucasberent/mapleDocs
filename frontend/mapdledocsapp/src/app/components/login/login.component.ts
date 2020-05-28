import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginDTO} from '../../dto/login-dto';
import {first} from 'rxjs/operators';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl = '/search';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthService,
    private toastrService:ToastrService
  ) {
    if (this.authenticationService.isLoggedIn()) {
      this.router.navigate([this.returnUrl]);
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.loginForm.invalid) {
      this.toastrService.error('invalid input')
      return;
    }

    this.loading = true;
    this.authenticationService.authenticate(new LoginDTO(this.f.username.value, this.f.password.value))
      .pipe(first())
      .subscribe(
        data => {
          this.toastrService.success('login successful, welcome');
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.toastrService.error('error logging in');
          console.log('error logging in: ' + error);
          this.loading = false;
        });
  }
}
