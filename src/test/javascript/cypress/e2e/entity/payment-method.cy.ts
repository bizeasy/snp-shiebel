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

describe('PaymentMethod e2e test', () => {
  const paymentMethodPageUrl = '/payment-method';
  let username: string;
  let password: string;
  const paymentMethodSample = { name: 'graffiti accomplished viv' };

  let paymentMethod;
  let paymentMethodType;

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
      url: '/api/payment-method-types',
      body: { name: 'nor where but', description: 'failing' },
    }).then(({ body }) => {
      paymentMethodType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-methods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-methods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-methods/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payment-method-types', {
      statusCode: 200,
      body: [paymentMethodType],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (paymentMethod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-methods/${paymentMethod.id}`,
      }).then(() => {
        paymentMethod = undefined;
      });
    }
  });

  afterEach(() => {
    if (paymentMethodType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-method-types/${paymentMethodType.id}`,
      }).then(() => {
        paymentMethodType = undefined;
      });
    }
  });

  it('PaymentMethods menu should load PaymentMethods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-method');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentMethod').should('exist');
    cy.location('pathname').should('eq', paymentMethodPageUrl);
  });

  describe('PaymentMethod page', () => {
    it('should have translated page title', () => {
      cy.visit(paymentMethodPageUrl);
      cy.getEntityHeading('PaymentMethod').should('not.contain', 'snpApp.paymentMethod.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentMethodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentMethod page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${paymentMethodPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PaymentMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-methods',
          body: {
            ...paymentMethodSample,
            paymentMethodType,
          },
        }).then(({ body }) => {
          paymentMethod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-methods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-methods?page=0&size=20>; rel="last",<http://localhost/api/payment-methods?page=0&size=20>; rel="first"',
              },
              body: [paymentMethod],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentMethodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentMethod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentMethod');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodPageUrl);
      });

      it('edit button click should load edit PaymentMethod page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodPageUrl);
      });

      it('edit button click should load edit PaymentMethod page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentMethod');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodPageUrl);
      });

      it('last delete button click should delete instance of PaymentMethod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentMethod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodPageUrl);

        paymentMethod = undefined;
      });
    });
  });

  describe('new PaymentMethod page', () => {
    beforeEach(() => {
      cy.visit(paymentMethodPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentMethod');
    });

    it('should create an instance of PaymentMethod', () => {
      cy.get(`[data-cy="name"]`).type('cornet pish slope');
      cy.get(`[data-cy="name"]`).should('have.value', 'cornet pish slope');

      cy.get(`[data-cy="description"]`).type('geez alongside');
      cy.get(`[data-cy="description"]`).should('have.value', 'geez alongside');

      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T08:58');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T08:58');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T19:40');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T19:40');

      cy.get(`[data-cy="paymentMethodType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paymentMethod = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', paymentMethodPageUrl);
    });
  });
});
