import {
  Component,
  OnInit,
  AfterViewInit,
  ElementRef,
  HostListener,
  OnDestroy,
  Renderer2,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';

interface MenuState {
  isActive: boolean;
  isOpen: boolean;
}

@Component({
  selector: 'app-user-header',
  standalone: false,
  templateUrl: './user-header.component.html',
  styleUrl: './user-header.component.scss',
})
export class UserHeaderComponent implements OnInit, AfterViewInit, OnDestroy {
  menuState: MenuState = {
    isActive: false,
    isOpen: false,
  };

  isDropdownOpen = false;
  selectedDropdown = 'English';

  @ViewChild('menuContent', { static: false }) menuContentRef!: ElementRef;
  @ViewChild('menuTrigger', { static: false }) menuTriggerRef!: ElementRef;
  @ViewChild('dropdownPlaceholder', { static: false })
  dropdownPlaceholderRef!: ElementRef;
  @ViewChild('dropdownList', { static: false }) dropdownListRef!: ElementRef;

  private eventListeners: Array<() => void> = [];

  constructor(private router: Router, private renderer: Renderer2) {}

  goToLogin() {
    this.router.navigate(['/auth/login']);
  }

  goToRegister() {
    this.router.navigate(['/auth/register']);
  }

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.initDropdown();
    this.initMenuToggle();
  }

  ngOnDestroy(): void {
    this.eventListeners.forEach((off) => off());
  }

  @HostListener('window:resize')
  onResize() {
    if (window.innerWidth > 991 && this.menuState.isOpen) {
      this.togglePageMenu(false);
    }
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const dropdownList = this.dropdownListRef?.nativeElement;
    const dropdownPlaceholder = this.dropdownPlaceholderRef?.nativeElement;

    if (
      dropdownList &&
      dropdownPlaceholder &&
      !dropdownList.contains(target) &&
      !dropdownPlaceholder.contains(target)
    ) {
      this.toggleDropdown(false);
    }
  }

  private initDropdown(): void {
    const placeholder = this.dropdownPlaceholderRef?.nativeElement;
    const list = this.dropdownListRef?.nativeElement;

    if (placeholder && list) {
      const clickHandler = this.renderer.listen(
        placeholder,
        'click',
        (e: Event) => {
          e.stopPropagation();
          this.toggleDropdown();
        }
      );
      this.eventListeners.push(clickHandler);

      const items = list.querySelectorAll('a');
      items.forEach((item: Element) => {
        const handler = this.renderer.listen(item, 'click', (e: Event) => {
          e.preventDefault();
          this.selectDropdownItem(item.textContent || '');
        });
        this.eventListeners.push(handler);
      });
    }
  }

  private initMenuToggle(): void {
    const trigger = this.menuTriggerRef?.nativeElement;
    const menu = this.menuContentRef?.nativeElement;

    if (trigger && menu) {
      const triggerClick = this.renderer.listen(trigger, 'click', () => {
        this.togglePageMenu();
      });
      this.eventListeners.push(triggerClick);
    }
  }

  private toggleDropdown(force?: boolean): void {
    const list = this.dropdownListRef?.nativeElement;
    if (!list) return;

    const shouldOpen =
      force !== undefined ? force : !list.classList.contains('active');
    if (shouldOpen) {
      this.renderer.addClass(list, 'active');
    } else {
      this.renderer.removeClass(list, 'active');
    }
  }

  private selectDropdownItem(text: string): void {
    const placeholder = this.dropdownPlaceholderRef?.nativeElement;
    if (!placeholder) return;

    placeholder.textContent = text;
    placeholder.style.opacity = '1';
    this.toggleDropdown(false);
  }

  private togglePageMenu(open?: boolean): void {
    const menu = this.menuContentRef?.nativeElement;
    if (!menu) return;

    const shouldOpen = open !== undefined ? open : !this.menuState.isOpen;
    this.menuState.isOpen = shouldOpen;
    this.menuState.isActive = shouldOpen;

    this.renderer.setStyle(menu, 'transition', 'height 0.3s ease');
    this.renderer.setStyle(menu, 'height', shouldOpen ? 'auto' : '0px');
  }

  active(): void {
    console.log(1);
  }
}
