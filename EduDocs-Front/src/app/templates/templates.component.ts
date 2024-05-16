import { Component } from '@angular/core';
import { BackendService } from '../services/backend.service';
import { StorageService, AdministrationRole, Template, getLocaleAdministartionRoles, Locale } from '../services/storage.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-templates',
  templateUrl: './templates.component.html',
  styleUrls: ['./templates.component.css']
})
export class TemplatesComponent {
  protected currentTemplateIndex = 0

  protected adminRoles: string[] = Locale.getLocaleAdministartionRoles()

  private id: string | undefined;

  protected name: string = ""
  protected respAdmin: string = ""
  protected routeToDocument: string = ""

  constructor(private backend: BackendService, protected storage: StorageService, private auth: AuthService) {
    backend.getTemplates().subscribe({
      next: (templates) => this.storage.templates = templates
    })
  }

  onTemplateChange() {
    var template = this.storage.templates[this.currentTemplateIndex]
    if (template != undefined) {
      this.id = template.uuid
      this.name = template.name
      this.routeToDocument = template.routeToDocument
      this.respAdmin = Locale.getLocaleAdministartionRoles()[template.responsibleAdmin]
    }
  }

  submit() {
    var template: Template = { uuid: this.id, name: this.name, responsibleAdmin: this.adminRoles.indexOf(this.respAdmin), routeToDocument: this.routeToDocument }
    console.log(template)
    this.backend.addTemplate(template).subscribe(
      {
        next: (templates) => this.storage.templates = templates
      }
    )
  }
}
