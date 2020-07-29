import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {first} from 'rxjs/operators';
import {ToastrService} from "ngx-toastr";
import {RegisterDto} from "../../dto/register-dto";

@Component({
  selector: 'app-login',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl = '/login';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthService,
    private toastrService: ToastrService
  ) {
    if (this.authenticationService.isLoggedIn()) {
      this.router.navigate([this.returnUrl]);
    }
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }


    this.loading = true;
    this.authenticationService.register(
      new RegisterDto(
        this.f.username.value,
        this.f.password.value))
      .pipe(first())
      .subscribe(
        data => {
          this.toastrService.success('successfully registered');
          this.router.navigate([this.returnUrl]);
        },
        error => {
          console.log('error registering');
          if (error.error.errors[0].arguments[0].code === 'password') {
            this.toastrService.error('Invalid password must be at least 8 digits long');
          } else {
            this.toastrService.error('error registering');
            console.log(error);
          }
          this.loading = false;
        });
  }
}
