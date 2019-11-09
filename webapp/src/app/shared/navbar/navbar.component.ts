import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../core/authentication.service';
import {Router} from '@angular/router';

@Component({
  selector: 'openmeed-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  state = 'Logout';

  constructor(private authenticationService: AuthenticationService, private router: Router) {
  }

  ngOnInit() {
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigateByUrl('/login');
  }

}
