import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../service/auth.service';
import {FormBuilder, FormGroup, ValidatorFn, Validators} from '@angular/forms';
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
      password: ['', Validators.required],
      doiServiceUsername: [],
      doiServiceDoiPrefix: []
    }, IfOneBothFilledOutValidator);
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
        this.f.password.value,
        this.f.doiServiceUsername.value,
        this.f.doiServiceDoiPrefix.value))
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

export const IfOneBothFilledOutValidator = (): ValidatorFn => {

  return (group: FormGroup): { [key: string]: boolean } => {

    let username;
    let doiPrefix;

    if (group.controls.hasOwnProperty('doiServiceUsername')) {
      username = group.controls.doiServiceUsername;
    }
    if (group.controls.hasOwnProperty('doiServiceDoiPrefix')) {
      doiPrefix = group.controls.doiServiceDoiPrefix;
    }

    if ((!username && doiPrefix) || (!doiPrefix && username)) {
      return {ifOneBothFilledOut: false};
    } else {
      return {ifOneBothFilledOut: true};
    }
  };
};
