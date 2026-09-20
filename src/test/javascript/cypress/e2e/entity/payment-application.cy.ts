import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PaymentApplication e2e test', () => {
  const paymentApplicationPageUrl = '/payment-application';
  let username: string;
  let password: string;
  const paymentApplicationSample = { amountApplied: 11316.94 };

  let paymentApplication;
  let payment;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/payments',
      body: {
        paymentRefNumber: 'punctuation livid',
        effectiveDate: '2023-12-14T21:28:13.295Z',
        paymentDate: '2023-12-14T06:40:10.351Z',
        amount: 7682.64,
        actualAmount: 13742.25,
        paymentStatus: 'blah worth',
        mihpayId: 'sham greatly phooey',
        txnId: 'boo premier condense',
        email: 'Tamia27@yahoo.com',
        phone: '298-854-8353 x98244',
        productInfo: 'ah',
      },
    }).then(({ body }) => {
      payment = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-applications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-applications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-applications/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payments', {
      statusCode: 200,
      body: [payment],
    });

    cy.intercept('GET', '/api/invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/invoice-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (paymentApplication) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-applications/${paymentApplication.id}`,
      }).then(() => {
        paymentApplication = undefined;
      });
    }
  });

  afterEach(() => {
    if (payment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payments/${payment.id}`,
      }).then(() => {
        payment = undefined;
      });
    }
  });

  it('PaymentApplications menu should load PaymentApplications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-application');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentApplication').should('exist');
    cy.location('pathname').should('eq', paymentApplicationPageUrl);
  });

  describe('PaymentApplication page', () => {
    it('should have translated page title', () => {
      cy.visit(paymentApplicationPageUrl);
      cy.getEntityHeading('PaymentApplication').should('not.contain', 'snpApp.paymentApplication.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentApplicationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentApplication page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${paymentApplicationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PaymentApplication');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentApplicationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-applications',
          body: {
            ...paymentApplicationSample,
            payment,
          },
        }).then(({ body }) => {
          paymentApplication = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-applications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-applications?page=0&size=20>; rel="last",<http://localhost/api/payment-applications?page=0&size=20>; rel="first"',
              },
              body: [paymentApplication],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentApplicationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentApplication page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentApplication');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentApplicationPageUrl);
      });

      it('edit button click should load edit PaymentApplication page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentApplication');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentApplicationPageUrl);
      });

      it('edit button click should load edit PaymentApplication page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentApplication');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentApplicationPageUrl);
      });

      it('last delete button click should delete instance of PaymentApplication', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentApplication').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentApplicationPageUrl);

        paymentApplication = undefined;
      });
    });
  });

  describe('new PaymentApplication page', () => {
    beforeEach(() => {
      cy.visit(paymentApplicationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentApplication');
    });

    it('should create an instance of PaymentApplication', () => {
      cy.get(`[data-cy="amountApplied"]`).type('9763.28');
      cy.get(`[data-cy="amountApplied"]`).should('have.value', '9763.28');

      cy.get(`[data-cy="payment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paymentApplication = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', paymentApplicationPageUrl);
    });
  });
});
