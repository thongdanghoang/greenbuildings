import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {
  EnterpriseDetailDTO,
  EnterpriseService
} from '../../services/enterprise.service';
import {finalize} from 'rxjs/operators';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-enterprise-profile',
  templateUrl: './enterprise-profile.component.html',
  styleUrl: './enterprise-profile.component.css',
  providers: [MessageService]
})
export class EnterpriseProfileComponent implements OnInit {
  profileForm!: FormGroup;
  loading = false;
  submitting = false;
  enterpriseProfile: EnterpriseDetailDTO | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly enterpriseService: EnterpriseService,
    private readonly messageService: MessageService,
    private readonly translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadEnterpriseProfile();
  }

  // Helper method to check if a form field is invalid
  isInvalid(fieldName: string): boolean {
    const field = this.profileForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }

  // Helper method for getting specific error for a field
  getErrorMessage(fieldName: string): string {
    const field = this.profileForm.get(fieldName);
    if (field?.hasError('required')) {
      return this.translateService.instant('validation.required');
    }
    if (field?.hasError('email')) {
      return this.translateService.instant('validation.email');
    }
    if (field?.hasError('pattern')) {
      return this.translateService.instant('validation.phoneNumber');
    }
    return '';
  }

  // eslint-disable-next-line max-lines-per-function
  onSubmit(): void {
    if (this.profileForm.invalid || !this.enterpriseProfile) {
      this.profileForm.markAllAsTouched();
      return;
    }

    this.submitting = true;

    const updatedProfile: EnterpriseDetailDTO = {
      ...this.enterpriseProfile,
      name: this.profileForm.value.name,
      email: this.profileForm.value.email,
      hotline: this.profileForm.value.hotline
    };

    this.enterpriseService
      .updateEnterpriseProfile(updatedProfile)
      .pipe(finalize(() => (this.submitting = false)))
      .subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: this.translateService.instant('common.success'),
            detail: this.translateService.instant('common.saveSuccess')
          });
          this.loadEnterpriseProfile(); // Refresh the profile data
        },
        error: error => {
          this.messageService.add({
            severity: 'error',
            summary: this.translateService.instant('common.error.title'),
            detail: this.translateService.instant('common.error.businessError')
          });
          console.error('Error updating enterprise profile:', error);
        }
      });
  }

  private initForm(): void {
    this.profileForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      hotline: [
        '',
        [Validators.required, Validators.pattern('^(\\+84|84|0)[0-9]{9}$')]
      ]
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  loadEnterpriseProfile(): void {
    this.loading = true;
    this.enterpriseService
      .getEnterpriseProfile()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: profile => {
          this.enterpriseProfile = profile;
          this.profileForm.patchValue({
            name: profile.name,
            email: profile.email,
            hotline: profile.hotline
          });
        },
        error: error => {
          this.messageService.add({
            severity: 'error',
            summary: this.translateService.instant('common.error.title'),
            detail: this.translateService.instant('common.error.businessError')
          });
          console.error('Error loading enterprise profile:', error);
        }
      });
  }
}
