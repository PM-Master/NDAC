import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';
import {PmHealthService} from '../../services/pmhealth.service';
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {

  constructor(private pmhealth: PmHealthService,
              private router: Router,
              private alert: AlertService) { }

  ngOnInit() {
  }

  login(form: NgForm) {
    const username = form.value.username;
    const password = form.value.password;

    this.pmhealth.login(username, password)
      .then(res => {
        console.log(res);
        if (res === null) {
          this.alert.error('Error loging in. Check username and password.');
        } else {
          this.router.navigate(['/dashboard']);
        }
      });


  }

}
