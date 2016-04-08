class java
{
  include apt

  apt::ppa { 'ppa:webupd8team/java': } ->
  exec { 'accept-license':
    path => '/bin:/usr/bin',
    command => 'echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections',
  } ->
  package { 'oracle-java8-installer':
    ensure => present
  }
}
