import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'openmeed-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.scss']
})
export class RedirectComponent implements OnInit {

  constructor(private router: Router, private activatedRoutes: ActivatedRoute) {
  }

  ngOnInit() {
    alert("we are in");
    this.router.navigateByUrl('/dashboard')

    const token = this.activatedRoutes.snapshot.queryParamMap.get("token");
    const access_token = this.activatedRoutes.snapshot.queryParamMap.get("access_token");
    const roles = this.activatedRoutes.snapshot.queryParamMap.get("roles");
    const username = this.activatedRoutes.snapshot.queryParamMap.get("username");
    console.log(token,access_token,roles,username)
    if (access_token != null) {
      window.sessionStorage.setItem('github_access_token', access_token);
      window.sessionStorage.setItem('access_token', token);
      window.sessionStorage.setItem('roles', roles);
      window.sessionStorage.setItem('username', username);
      this.router.navigateByUrl('/dashboard')
    } else {
      //this.router.navigateByUrl('/login')
    }
  }

}
